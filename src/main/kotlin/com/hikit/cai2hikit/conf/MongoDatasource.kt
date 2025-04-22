package com.hikit.cai2hikit.conf

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.Conventions
import org.bson.codecs.pojo.PojoCodecProvider
import org.hikit.common.datasource.Datasource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class MongoDatasource @Autowired constructor(@Value("\${spring.data.mongodb.uri}") mongoDbUri: String) : Datasource {

    private lateinit var mongoClient: MongoClient

    @Value("\${spring.data.mongodb.database}")
    private lateinit var databaseName: String

    init {
        val pojoCodecRegistry =
            CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(
                    PojoCodecProvider.builder().automatic(true)
                        .register("com.hikit.cai2hikit.dao")
                        .conventions(Conventions.DEFAULT_CONVENTIONS)
                        .build()
                )
            )
        val mongoSettings: MongoClientSettings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .applyConnectionString(
                ConnectionString(mongoDbUri)
            )
            .build()
        mongoClient = MongoClients.create(mongoSettings)
    }

    override fun getClient(): MongoClient? {
        return mongoClient
    }

    override fun getDB(): MongoDatabase {
        return mongoClient.getDatabase(databaseName)
    }

    override fun getDBName(): String? {
        return databaseName
    }
}