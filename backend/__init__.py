from contextlib import asynccontextmanager

from fastapi import FastAPI

from backend.core.init_app import create_middleware, init_data
from tortoise import Tortoise

from backend.settings.config import SETTINGS


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
    return app


app = create_app()


@app.get("/")
async def root():
    return {"message": "Hello World"}
