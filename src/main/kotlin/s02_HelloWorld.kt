import aws.sdk.kotlin.services.dynamodb.DynamoDbClient

suspend fun main() {
    val dynamoDbClient = DynamoDbClient.fromEnvironment { }
    val response = dynamoDbClient.listTables { }
    println("Found tables:")
    response.tableNames?.forEach { println("Table: $it") }
}
