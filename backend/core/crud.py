from typing import NewType, TypeVar, Generic, Type, Dict, Union, Any, Tuple, List

from pydantic import BaseModel
from tortoise import Model
from tortoise.expressions import Q


# Total 是一个特殊的 int 类型，用于类型检查，运行时仍然是 int
Total = NewType("Total", int)

# 2. 定义泛型类型变量
ModelType = TypeVar("ModelType", bound=Model)
# 限制：必须是 Model 或其子类（Tortoise ORM 模型）post里面的json
CreateSchemaType = TypeVar("CreateSchemaType", bound=BaseModel)
UpdateSchemaType = TypeVar("UpdateSchemaType", bound=BaseModel)


# 这个类是一个泛型类，接受三个类型参数：
# - ModelType: ORM 模型类型 表
# - CreateSchemaType: 创建数据用的 Pydantic Schema
# - UpdateSchemaType: 更新数据用的 Pydantic Schema
class CRUDBase(Generic[ModelType, CreateSchemaType, UpdateSchemaType]):
    def __init__(self, model: Type[ModelType]):
        self.model = model

    async def get(self, id: int) -> ModelType:
        return await self.model.get(id=id)

    async def create(self, data: CreateSchemaType) -> ModelType:
        # dict相当于json
        if isinstance(data, Dict):
            obj_dict = data
        else:
            obj_dict = data.model_dump()
        # 讲json里面打散赋值给model
        obj = self.model(**obj_dict)
        # 耗时活动
        await obj.save()
        # 创建好后有默认值
        return obj

    async def update(self, id: int, data: Union[UpdateSchemaType, Dict[str, Any]]) -> ModelType:
        if isinstance(data, Dict):
            obj_dict = data
        else:
            # exclude_unset=True 排除使用默认值的字段
            # 排除id字段
            obj_dict = data.model_dump(exclude_unset=True, exclude={"id"})
        obj = await self.model.get(id=id)
        obj = obj.update_from_dict(obj_dict)
        await obj.save()
        return obj

    async def delete(self, id: int) -> None:
        obj = await self.get(id)
        await obj.delete()

    # tuple() 不可变
    # list[] 数组 可变
    async def list(self, page: int, page_size: int, search: Q = Q(), order: list = []) -> Tuple[Total, List[ModelType]]:
        query = self.model.filter(search)
        data_list = await query.offset((page - 1) * page_size).limit(page_size).order_by(*order)
        data_count = await query.count()
        return data_count, data_list
