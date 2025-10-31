from datetime import datetime

from pydantic import BaseModel


class CredentialSchemaAdmin(BaseModel):
    username: str
    password: str


class JWTPayload(BaseModel):
    user_id: int
    username: str
    # 签发时间
    iat: datetime
    # 过期时间
    exp: datetime
