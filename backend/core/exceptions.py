# 未找到
from enum import Enum

from fastapi import HTTPException
from fastapi.requests import Request
from starlette.responses import JSONResponse
from tortoise.exceptions import DoesNotExist


async def exception_404_handler(req: Request, exc: DoesNotExist) -> JSONResponse:
    content = dict(
        code=404,
        msg=f"exc:${exc},params:${req.query_params}",
    )
    return JSONResponse(content, status_code=404)


async def exception_custom_handler(_: Request, exc: HTTPException) -> JSONResponse:
    print("exception_custom_handler")
    print(exc)
    content = dict(
        code=exc.status_code,
        msg=exc.detail,
    )
    return JSONResponse(content, status_code=500)

async def exception_system_handler(_: Request, exc: Exception) -> JSONResponse:
    content = dict(
        code=500,
        msg="系统错误",
    )
    return JSONResponse(content, status_code=500)


class ErrorCode(Enum):
    # 通用错误 (1000-1999)
    UNKNOWN_ERROR = 1000
    VALIDATION_ERROR = 1001
    DATABASE_ERROR = 1002
    NETWORK_ERROR = 1003

    # 认证错误 (2000-2999)
    UNAUTHORIZED = 2000
    INVALID_TOKEN = 2001
    TOKEN_EXPIRED = 2002
    PERMISSION_DENIED = 2003
