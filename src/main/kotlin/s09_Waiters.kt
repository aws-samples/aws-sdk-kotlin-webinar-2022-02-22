import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import aws.sdk.kotlin.services.dynamodb.waiters.waitUntilTableExists

suspend fun main() {
    val ddb = DynamoDbClient.fromEnvironment { }
    val table = "waiters-demo-2"

    println("Creating table...")
    ddb.createTable {
        tableName = table
        attributeDefinitions = listOf(
            AttributeDefinition {
                attributeName = "id"
                attributeType = ScalarAttributeType.S
            }
        )
        keySchema = listOf(
            KeySchemaElement {
                attributeName = "id"
                keyType = KeyType.Hash
            }
        )
        billingMode = BillingMode.PayPerRequest
    }
    println("Create table API call is done")

    ddb.waitUntilTableExists { tableName = table }
    println("Table is *actually* created!")

    println("Putting item...")
    ddb.putItem {
        tableName = table
        item = mapOf("id" to AttributeValue.S("foo"))
    }
    println("Put item done!")
}
