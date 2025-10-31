from datetime import datetime

from fastapi import HTTPException

from backend.core.crud import CRUDBase
from backend.core.exceptions import ErrorCode
from backend.models import Admin
from backend.schemas.admins import AdminCreate, AdminUpdate
from backend.schemas.login import CredentialSchemaAdmin
from backend.utils.password import get_password_hash
from backend.utils.password import verify_password


class AdminControl(CRUDBase[Admin, AdminCreate, AdminUpdate]):
    def __init__(self):
        super().__init__(model=Admin)

    async def get_by_username(self, username):
        return await self.model.filter(username=username).first()

    async def authenticate(self, credential: CredentialSchemaAdmin):
        admin = await self.get_by_username(credential.username)
        if not admin:
            raise HTTPException(status_code=ErrorCode.VALIDATION_ERROR.value, detail="Admin not found")
        verify_result = verify_password(credential.password, admin.password)
        if not verify_result:
            raise HTTPException(status_code=ErrorCode.VALIDATION_ERROR.value, detail="Incorrect password")
        if not admin.is_active:
            raise HTTPException(status_code=ErrorCode.VALIDATION_ERROR.value, detail="Inactive user")
        return admin

    async def update_last_login(self, user_id: int) -> None:
        user = await self.model.get(id=user_id)
        user.last_login = datetime.now()
        await user.save()

    async def create_admin(self, obj_in: AdminCreate):
        # 密码hash一下
        obj_in.password = get_password_hash(obj_in.password)
        return await self.create(obj_in)


admin_control = AdminControl()
