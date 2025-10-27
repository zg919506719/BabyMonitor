import asyncio
from datetime import datetime

from tortoise import models, fields

from backend import SETTINGS


class BaseModel(models.Model):
    # id自增长
    id = fields.BigIntField(pk=True, index=True)

    # 转化成map
    async def to_dict(self, m2m: bool = False, exclude_fields: list[str] | None = None):
        if exclude_fields is None:
            exclude_fields = []

        d = {}
        for field in self._meta.db_fields:
            # 剔除字段
            if field not in exclude_fields:
                value = getattr(self, field)
                if isinstance(value, datetime):
                    value = value.strftime(SETTINGS.DATETIME_FORMAT)
                d[field] = value
        # 如果包含多对多
        if m2m:
            tasks = [
                self.__fetch_m2m_field(field, exclude_fields)
                for field in self._meta.m2m_fields
                if field not in exclude_fields
            ]
            results = await asyncio.gather(*tasks)
            for field, values in results:
                d[field] = values

        return d

    async def __fetch_m2m_field(self, field, exclude_fields):
        values = await getattr(self, field).all().values()
        formatted_values = []

        for value in values:
            formatted_value = {}
            for k, v in value.items():
                if k not in exclude_fields:
                    if isinstance(v, datetime):
                        # 时间转化
                        formatted_value[k] = v.strftime(SETTINGS.DATETIME_FORMAT)
                    else:
                        formatted_value[k] = v
            formatted_values.append(formatted_value)

        return field, formatted_values

    # 这是一个抽象基类 不会在数据库中创建实际的表，而是作为其他模型的模板。
    class Meta:
        abstract = True


class UUIDModel:
    # 随机数 可做唯一标志？
    uuid = fields.UUIDField(unique=True, pk=False, index=True)


class TimestampMixin:
    # 创建之间和更新时间
    created_at = fields.DatetimeField(auto_now_add=True, index=True)
    updated_at = fields.DatetimeField(auto_now=True, index=True)
