Goal of the project:
- implement example of relational database sharding
- create a service that utilizes multiple datasources but behaves like a single datasource using simple data model example

Implementation details:
- Enabled spring to connect to multiple datasources
- Implemented ability to programmatically change datasources using AbstractRoutingDataSource and ThreadLocal context
- Replaced default datasource in spring context with AbstractRoutingDataSource
- Implemented method for creating shardkeys to properly identify objects and show which shard they belong to
- Implemented multiple embedded H2 databases to properly test the bahavior of multi datasource routing

What I learned
- How to implement AbstractRoutingDataSource
- How to implement multiple H2 database using EmbeddedDatabase instead of relying of spring boot to auto create
- How to use threadlocal as a global context
  
