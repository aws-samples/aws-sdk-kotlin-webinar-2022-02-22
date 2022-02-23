import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.paginators.items
import aws.sdk.kotlin.services.dynamodb.paginators.scanPaginated
import aws.smithy.kotlin.runtime.util.length

suspend fun main() {
    val ddb = DynamoDbClient.fromEnvironment { }

    var resp = ddb.scanPaginated {
        tableName = "world"
    }
    resp.items().collect { println(it["name"]) }
}
