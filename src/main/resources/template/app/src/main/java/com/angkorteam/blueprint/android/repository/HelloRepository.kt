package ${pkg}.repository

import ${pkg}.common.DataState
import ${pkg}.dao.HelloDao
import ${pkg}.entity.HelloEntity
import ${pkg}.network.HelloService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HelloRepository(
    private val dao: HelloDao,
    private val service: HelloService,
) {

    fun loadMore(key: String, lastId: Int): Flow<DataState<List<HelloEntity>>> = flow {
        try {
            emit(DataState.loading())
            var cache = dao.findByLastId(lastId = lastId, limit = 40)
            if (cache.isEmpty()) {
                var items = service.list(key = key, count = 40)
                items.forEach {
                    var entity = HelloEntity(
                        name = it.name,
                        description = it.description,
                        avatar = it.avatar,
                        dateOfBirth = "",
                        gender = "",
                        phrase = "",
                        id = null,
                    );
                    dao.insert(entity)
                }
                cache = dao.findByLastId(lastId = lastId, limit = 40)
            }
            emit(DataState.success(data = cache))
        } catch (e: Exception) {
            emit(DataState.error<List<HelloEntity>>(e.message ?: "Unknown Error"))
        }
    }

    fun loadDetail(
        entityId: Int
    ): Flow<DataState<HelloEntity>> = flow {
        try {
            emit(DataState.loading())

            delay(1000)

            var entity = dao.findById(entityId)

            entity?.let {
                emit(DataState.success(it))
            } ?: run {
                emit(DataState.error<HelloEntity>(message = "Unknown Error"))
            }
        } catch (e: Exception) {
            emit(DataState.error<HelloEntity>(e.message ?: "Unknown Error"))
        }
    }

}