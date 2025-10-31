import jwt

from backend import SETTINGS
from backend.schemas.login import JWTPayload


def create_access_token(data: JWTPayload) -> str:
    # 为啥要copy # 原始 data 完全不受影响  不copy可能修改原始数据"""
    payload = data.model_dump().copy()
    encoded_jwt = jwt.encode(payload=payload, key=SETTINGS.SECRET_KEY, algorithm=SETTINGS.JWT_ALGORITHM)
    return encoded_jwt
