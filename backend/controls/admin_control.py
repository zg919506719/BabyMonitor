from backend.core.crud import CRUDBase
from backend.models import Admin
from backend.schemas.admins import AdminCreate, AdminUpdate
from backend.utils.password import get_password_hash


class AdminControl(CRUDBase[Admin, AdminCreate, AdminUpdate]):
    def __init__(self):
        super().__init__(model=Admin)

    async def get_by_username(self, username):
        return await self.model.filter(username=username).first()

    async def create_admin(self, obj_in: AdminCreate):
        # 密码hash一下
        obj_in.password =get_password_hash(obj_in.password)
        return await self.create(obj_in)

admin_control = AdminControl()

