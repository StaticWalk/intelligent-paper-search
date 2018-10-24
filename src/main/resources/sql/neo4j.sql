

/*将author.csv引入到neo4j中，在Neo4j中创建Author节点**/
load csv with headers from "file:///author.csv" as line
merge(p:Author{id:toInteger(line.id),name:line.name,email:line.email,birth:line.birth});



/*将paper.csv引入到neo4j中，在Neo4j中创建Paper节点*/
oad csv with headers from "file:///paper.csv" as line
merge(p:Paper{id:toInteger(line.id),doi:line.doi,document_id:line.document_id,publisher:line.publisher,
publication_date:line.publication_date,abstract:line.abstract,keywords:line.keywords});


/*将author_paper.csv引入到neo4j，创建Author与Paper之间的create的relationship*/
load csv with headers from "file:///author_paper.csv" as line
match (from:Author{id:toInteger(line.author_id)}),(to:Paper{id:toInteger(line.paper_id)})
merge (from)-[r:create{author_id:toInteger(line.author_id),paper_id:toInteger(line.paper_id)}]->(to)


