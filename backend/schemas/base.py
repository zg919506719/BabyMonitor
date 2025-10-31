from typing import Optional, Any

from fastapi.responses import JSONResponse

from backend.core.exceptions import ErrorCode


class Success(JSONResponse):
    # **kwargs name="张三" key value 关键字参数（Keyword Arguments） 的缩写，用于接收任意数量的关键字参数。
    def __init__(self, code: int = 200, msg: Optional[str] = "OK", data: Optional[Any] = None, **kwargs):
        content = {"code": code, "msg": msg, "data": data}
        # {
        #     "code": 200,
        #     "msg": "success",
        #     "data": None,
        #     "timestamp": "2024-01-15",  # ✅ 新增的键
        #     "version": "1.0"            # ✅ 新增的键
        # }
        # 可能会添加这种的
        content.update(kwargs)
        super().__init__(content=content, status_code=code)


class Fail(JSONResponse):
    # **kwargs name="张三" key value 关键字参数（Keyword Arguments） 的缩写，用于接收任意数量的关键字参数。
    def __init__(self, code: int = ErrorCode.UNKNOWN_ERROR, msg: Optional[str] = "Error", data: Optional[Any] = None,
                 **kwargs):
        content = {"code": code, "msg": msg, "data": data}
        # {
        #     "code": 200,
        #     "msg": "success",
        #     "data": None,
        #     "timestamp": "2024-01-15",  # ✅ 新增的键
        #     "version": "1.0"            # ✅ 新增的键
        # }
        # 可能会添加这种的
        content.update(kwargs)
        super().__init__(content=content, status_code=code)
