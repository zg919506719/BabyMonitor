from fastapi import APIRouter

adminRoute = APIRouter()


@adminRoute.get("/")
async def index():
    return {"message": "Hello, admin!"}
