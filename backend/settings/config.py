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
                    "host": "localhost",  # Database host address
                    "port": 3306,  # Database port
                    "user": "root",  # Database username
                    "password": "admin",  # Database password
                    "database": "baby_monitor",  # Database name
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

SETTINGS = Settings()
