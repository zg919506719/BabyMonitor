import httpx
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.backends import default_backend
import base64
import logging

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class RSAEncryptor:
    def __init__(self, public_key_str: str):
        """
        初始化 RSA 加密器

        Args:
            public_key_str: 公钥字符串 (PEM 格式)
        """
        self.public_key = self._load_public_key(public_key_str)
        self.max_encrypt_size = 190  # 2048位RSA最大加密字节数 (针对OAEP padding)

    def _load_public_key(self, public_key_str: str):
        """加载公钥，支持多种格式"""
        try:
            # 清理公钥字符串
            cleaned_key = self._clean_public_key(public_key_str)

            # 加载公钥
            public_key = serialization.load_pem_public_key(
                cleaned_key.encode('utf-8'),
                backend=default_backend()
            )
            logger.info("公钥加载成功")
            return public_key

        except Exception as e:
            logger.error(f"公钥加载失败: {str(e)}")
            raise ValueError(f"无效的公钥: {str(e)}")

    def _clean_public_key(self, public_key_str: str) -> str:
        """清理公钥字符串格式"""
        # 移除多余的空格和换行
        cleaned = public_key_str.strip()

        # 确保有正确的头尾
        if not cleaned.startswith('-----BEGIN'):
            if 'PUBLIC KEY' in cleaned:
                # 尝试修复格式
                lines = cleaned.splitlines()
                if len(lines) > 1:
                    cleaned = f"-----BEGIN PUBLIC KEY-----\n{lines[0] if len(lines[0]) > 20 else ''.join(lines)}\n-----END PUBLIC KEY-----"
                else:
                    cleaned = f"-----BEGIN PUBLIC KEY-----\n{cleaned}\n-----END PUBLIC KEY-----"

        return cleaned

    def encrypt(self, plaintext: str) -> str:
        """
        加密文本

        Args:
            plaintext: 要加密的明文

        Returns:
            base64编码的加密字符串
        """
        try:
            # 检查数据长度
            plaintext_bytes = plaintext.encode('utf-8')
            if len(plaintext_bytes) > self.max_encrypt_size:
                raise ValueError(f"明文过长: {len(plaintext_bytes)} > {self.max_encrypt_size}")

            # 使用 OAEP 填充进行加密
            encrypted_data = self.public_key.encrypt(
                plaintext_bytes,
                padding.OAEP(
                    mgf=padding.MGF1(algorithm=hashes.SHA256()),
                    algorithm=hashes.SHA256(),
                    label=None
                )
            )

            # 返回 base64 编码
            encrypted_b64 = base64.b64encode(encrypted_data).decode('utf-8')
            logger.info("加密成功")
            return encrypted_b64

        except Exception as e:
            logger.error(f"加密失败: {str(e)}")
            raise RuntimeError(f"加密过程中出错: {str(e)}")

    def encrypt_with_pkcs1(self, plaintext: str) -> str:
        """
        使用 PKCS1v15 填充进行加密（兼容某些老系统）
        """
        try:
            plaintext_bytes = plaintext.encode('utf-8')
            max_size = 245  # PKCS1v15 允许稍大的数据

            if len(plaintext_bytes) > max_size:
                raise ValueError(f"明文过长: {len(plaintext_bytes)} > {max_size}")

            encrypted_data = self.public_key.encrypt(
                plaintext_bytes,
                padding.PKCS1v15()
            )

            return base64.b64encode(encrypted_data).decode('utf-8')

        except Exception as e:
            logger.error(f"PKCS1加密失败: {str(e)}")
            raise RuntimeError(f"PKCS1加密失败: {str(e)}")


if __name__ == '__main__':
    # 使用示例
    public_key_str = """-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCS+EFeqI1STM/9foHqn3CS6JrdbRclaLJbkS4qDLmPaiAIuc8YCr5tJiSyBeFvdcc0Co+/XUmLAqXySmk9+rDvZgt2EuEDhL9O8G4JvJSRunhdPy5I5wZ+GYhv3II+IAZu+SNsCPnBHskYJvNXS4IUMq1JCYDeDINylsdVE2kYhQIDAQAB
-----END PUBLIC KEY-----"""
    account="xjs"
    verifyCode="0000"
    key="38d2a5d859805d74b913b08c23ee4008"

    # 创建加密器
    encryptor = RSAEncryptor(public_key_str)

    encrypted_account = encryptor.encrypt_with_pkcs1(account)
    encrypted_verifyCode = encryptor.encrypt_with_pkcs1(verifyCode)
    encrypted_key = encryptor.encrypt_with_pkcs1(key)
    print(f"加密结果: {encrypted_account}")
    print(f"加密结果: {encrypted_verifyCode}")
    print(f"加密结果: {encrypted_key}")

    testAccount="OhMAOdCtbKg5hwPl5YtpnRg8gRtgOHiLqZrmzXYoXqHACW9aBqQgvoNQzvHSf5b2RL8BN8hnBYmJMXCW9z0NL5Q5sKSLKtZyP2dgnyyH6sRw/XgpuY7R7uQrdwpRui9IAVVI0hnfdcauNYMIINd7KBrEb7YJPlDXD2DZaV3wwgY="
    testVerifyCode="Yjbk/RObERbvC+pwEbBDvWxV0R5w2sjKqpeNBUbR4Ac+UcmHnCU+LDsxSkmr3XL32BLdu2+iTIY/YkHiFLuXhPEcihRhh3afNDFCdzh7tBTiCka9mRHBi9EwLFGsoGuuR85NxZciR2XKwfXw3rTgAlA21+qlA5A960PSVBmA5JM="
    testKey="TQ4tjjcAYkgI/wHPNjyFJ2jTw6E6W5O4gdop6DeynyhHZThxOalx5yYS1KRq/KJm0uZ/uXZGdumIgH5pb06lh5B3+k2ip2BPmox3r2wIf/cVOMHrSq0QjU9Q5ZypElcUo7i3LriYlaSDslzpKurBK5t/CyGXZdY3UUnvkcVEErw="
    try:
        with httpx.Client() as client:
            response = client.post(
                "https://admin.dev.cashcepat.id/admin-api/api/admin/login/verify-opt",
                json={"account": f"{encrypted_account}","verifyCode":f"{encrypted_verifyCode}"},  # 查询参数
                headers={"key": f"{encrypted_key}","Accept-Language":"en-US"},
                timeout=30.0
            )
            # response = client.post(
            #     "https://admin.dev.cashcepat.id/admin-api/api/admin/login/verify-opt",
            #     json={"account": f"{testAccount}", "verifyCode": f"{testVerifyCode}"},  # 查询参数
            #     headers={"key": f"{testKey}", "Accept-Language": "en-US","Accept":"application/json, text/plain, */*","User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) chrome/1 42.0.0.0 Safari/537.36"},
            #     timeout=30.0
            # )

            print(response.json())
            # if response.status_code == 200:
            #     return response.content
            # else:
            #     raise HTTPException(
            #         status_code=response.status_code,
            #         detail=f"第三方接口错误: {response.text}"
            #     )

    except httpx.RequestError as e:
        print(e)
