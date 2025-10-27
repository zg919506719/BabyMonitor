import logging
import sys


from loguru import logger as loguru_logger

from backend.settings.config import SETTINGS


class Loggin:
    def __init__(self) -> None:
        debug = SETTINGS.DEBUG
        if debug:
            self.level = "DEBUG"
        else:
            self.level = "INFO"

    def setup(self):
        loguru_logger.remove()
        # sys.stderr：Python 的标准错误输出流
        # 输出位置：控制台/终端
        # 持久化：不会保存到文件，程序结束后日志就消失了
        # 典型用途：开发环境调试
        loguru_logger.add(sink=sys.stderr, level=self.level)
        # 保存到当前目录的 app.log 文件
        loguru_logger.add("logs/app.log", level=self.level, rotation="100 MB")  # Output log messages to a file
        return loguru_logger


loggin = Loggin()
logger = loggin.setup()
