from datetime import timedelta, timezone, datetime

from fastapi import APIRouter

from backend import SETTINGS
from backend.controls.admin_control import admin_control
from backend.models import Admin
from backend.schemas.base import Success
from backend.schemas.login import CredentialSchemaAdmin, JWTPayload
from backend.utils.jwt_utils import create_access_token

adminRoute = APIRouter()


@adminRoute.get("/")
async def index():
    return {"message": "Hello, admin!"}


@adminRoute.post("/login")
async def login(credential_schema: CredentialSchemaAdmin):
    admin: Admin = await admin_control.authenticate(credential_schema)
    # 更新登录时间
    await admin_control.update_last_login(admin.id)

    access_token_expires = timedelta(days=SETTINGS.ACCESS_TOKEN_EXPIRE_DAYS)
    expire = datetime.now(timezone.utc) + access_token_expires
    access_token = create_access_token(
        JWTPayload(user_id=admin.id,
                   username=credential_schema.username,
                   # 签发时间
                   iat=datetime.now(timezone.utc),
                   # 过期时间
                   exp=expire))
    return Success(data=access_token)
