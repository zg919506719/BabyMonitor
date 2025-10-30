from datetime import datetime
from typing import Optional, List

from pydantic import BaseModel, Field


# post model
class BaseAdmin(BaseModel):
    id: int
    username: Optional[str] = None
    is_active: Optional[bool] = True
    is_superuser: Optional[bool] = False
    created_at: Optional[datetime]
    updated_at: Optional[datetime]
    last_login: Optional[datetime]


class AdminCreate(BaseModel):
    # Field    是一个函数，用于为模型字段提供额外的约束、验证和描述信息。
    # gt	Greater Than	大于	>	必须大于指定值
    # le	Less Than or Equal	小于等于	<=	必须小于或等于指定值
    username: str = Field(
        min_length=5,
        max_length=15,
        pattern=r"^[a-zA-Z0-9]+$",  # 只允许字母、数字、下划线
        description="用户名，3-50个字符"
    )
    password: str = Field(
        min_length=5,
        pattern=r"^[a-zA-Z0-9]+$",  # 只允许字母、数字、下划线
        description="密码至少8位，包含大小写字母和数字"
    )
    email: str = Field(examples=["admin@qq.com"])
    is_active: Optional[bool] = True
    is_superuser: Optional[bool] = False


class AdminUpdate(BaseModel):
    password: str = Field(
        min_length=5,
        pattern=r"^[a-zA-Z0-9]+$",  # 只允许字母、数字、下划线
        description="密码至少8位，包含大小写字母和数字"
    )
