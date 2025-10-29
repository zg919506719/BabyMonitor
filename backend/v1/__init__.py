from fastapi import APIRouter

from backend.v1.admin import adminRoute

v1_route = APIRouter()
v1_route.include_router(adminRoute, prefix="/admin", tags=["admin"])
