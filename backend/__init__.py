from contextlib import asynccontextmanager

from fastapi import FastAPI, APIRouter

from backend.core.init_app import create_middleware, init_data, register_exceptions
from tortoise import Tortoise

from backend.settings.config import SETTINGS
from backend.v1 import v1_route


@asynccontextmanager
async def lifespan(app: FastAPI):
    await init_data()
    yield
    await Tortoise.close_connections()


def create_app() -> FastAPI:
    app = FastAPI(
        title=SETTINGS.APP_NAME,
        description=SETTINGS.APP_DESCRIPTION,
        version=SETTINGS.VERSION,
        docs_url="/",
        openapi_url="/openapi.json",
        middleware=create_middleware(),
        lifespan=lifespan,
    )
    register_exceptions(app)
    return app


app = create_app()
app.include_router(v1_route, prefix="/api/v1")
