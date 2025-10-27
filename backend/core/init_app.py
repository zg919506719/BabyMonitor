import shutil

from aerich import Command
from starlette.middleware import Middleware
from starlette.middleware.cors import CORSMiddleware

from backend.log import logger
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
    pass


async def init_data():
    # 数据库初始化相关
    await init_db()
    await init_super_user()
