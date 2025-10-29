# 未找到
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
    content = dict(
        code=exc.status_code,
        msg=exc.detail,
    )
    return JSONResponse(content, status_code=exc.status_code)
