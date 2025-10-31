FROM python:3.11-slim

WORKDIR /backend

COPY requirements.txt ./
RUN pip install -r requirements.txt

#在镜像构建时执行，成为镜像的一部分
# 基础代码，会被 volume 覆盖
COPY backend ./backend

EXPOSE 8000

CMD ["uvicorn", "backend:app", "--host", "0.0.0.0", "--port", "8000"]

