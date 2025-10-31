import shutil

from aerich import Command
from fastapi import FastAPI, HTTPException
from starlette.middleware import Middleware
from starlette.middleware.cors import CORSMiddleware
from tortoise.exceptions import DoesNotExist

from backend.controls.admin_control import admin_control
from backend.core.exceptions import exception_404_handler, exception_custom_handler, exception_system_handler
from backend.log import logger
from backend.schemas.admins import AdminCreate
from backend.settings.config import SETTINGS


def create_middleware():
    middleware = [
        # 解决跨域问题
        Middleware(
            CORSMiddleware,
            allow_origins=["*"],
            allow_credentials=True,
            allow_methods=["*"],
            allow_headers=["*"],
        )
        # todo 上传文件请求大小限制
        # todo 后台任务
    ]
    return middleware


async def init_db():
    command = Command(tortoise_config=SETTINGS.TORTOISE_ORM)
    try:
        await command.init_db(safe=True)
    except FileExistsError:
        pass

    await command.init()
    logger.warning("init success")

    try:
        await command.migrate()
    except AttributeError:
        logger.warning("unable to retrieve model history from database, model history will be created from scratch")
        # 删除对应目录
        shutil.rmtree("migrations")
        await command.init_db(safe=True)
    await command.upgrade(run_in_transaction=True)


async def init_super_user():
    admin = await admin_control.model.exists()
    if not admin:
        await admin_control.create_admin(AdminCreate(
            username="admin",
            password="admin",
            email="admin@admin.com",
            is_superuser=True,
        ))


async def init_data():
    # 数据库初始化相关
    await init_db()
    # 创建一个超级管理员
    await init_super_user()


def register_exceptions(app: FastAPI):
    app.add_exception_handler(DoesNotExist, exception_404_handler)
    # 自定义
    app.add_exception_handler(HTTPException, exception_custom_handler)
    app.add_exception_handler(Exception, exception_system_handler)
