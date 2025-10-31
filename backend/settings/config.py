import os

from pydantic.v1 import BaseSettings


class Settings(BaseSettings):
    VERSION: str = "0.0.1"
    APP_NAME: str = "BabyMonitor"
    PROJECT_NAME: str = "BabyMonitor Project"
    APP_DESCRIPTION: str = "BabyMonitor app"

    DEBUG: bool = True

    TORTOISE_ORM: dict = {
        "connections": {
            # MySQL/MariaDB configuration
            # Install with: tortoise-orm[asyncmy]
            "mysql": {
                "engine": "tortoise.backends.mysql",
                "credentials": {
                    "host": os.getenv("MYSQL_HOST"),  # Database host address
                    "port": 3306,  # Database port
                    # 注意空格
                    "user": os.getenv("MYSQL_USER").strip(),  # Database username
                    "password": os.getenv("MYSQL_PASSWORD").strip(),  # Database password
                    "database": os.getenv("MYSQL_DATABASE").strip(),  # Database name
                },
            },
        },
        "apps": {
            "models": {
                "models": ["backend.models", "aerich.models"],
                "default_connection": "mysql",
            },
        },
        "use_tz": False,  # Whether to use timezone-aware datetimes
        "timezone": "Asia/Shanghai",  # Timezone setting
    }
    # 数据库转化时间格式
    DATETIME_FORMAT: str = "%Y-%m-%d %H:%M:%S"
    # TOKEN 过期时间
    ACCESS_TOKEN_EXPIRE_DAYS = 7
    # jwt私钥
    SECRET_KEY: str = "3488a63e1765035d386f05409663f55c83bfae3b3c61a932744b20ad14244dcf"  # openssl rand -hex 32
    JWT_ALGORITHM: str = "HS256"


SETTINGS = Settings()
