import uvicorn
from uvicorn.config import LOGGING_CONFIG

if __name__ == '__main__':
    # todo log_config相关配置 log_config=LOGGING_CONFIG
    uvicorn.run("backend:app", host="0.0.0.0", port=8000, reload=True)
