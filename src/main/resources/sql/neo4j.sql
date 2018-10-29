

/*将author.csv引入到neo4j中，在Neo4j中创建Author节点**/
load csv with headers from "file:///author.csv" as line
merge(p:Author{id:toInteger(line.id),name:line.name,email:line.email,birth:line.birth});


/*将paper.csv引入到neo4j中，在Neo4j中创建Paper节点*/
load csv with headers from "file:///paper.csv" as line
merge(p:Paper{id:toInteger(line.id),name:line.name,doi:line.doi,document_id:line.document_id,publisher:line.publisher,
publication_date:line.publication_date,introduction:line.introduction});


/*将keyword.csv引入到neo4j中，在Neo4j中创建Keyword节点**/
load csv with headers from "file:///keyword.csv" as line
merge(p:Keyword{id:toInteger(line.id),name:line.name});


/*将author_paper.csv引入到neo4j，创建Author与Paper之间的create的relationship*/
load csv with headers from "file:///author_paper.csv" as line
match (from:Author{id:toInteger(line.author_id)}),(to:Paper{id:toInteger(line.paper_id)})
merge (from)-[r:create{author_id:toInteger(line.author_id),paper_id:toInteger(line.paper_id)}]->(to);


/*将paper_keyword.csv引入到neo4j，创建paper与keyword之间的attribute的relationship*/
load csv with headers from "file:///paper_keyword.csv" as line
match (from:Paper{id:toInteger(line.paper_id)}),(to:Keyword{id:toInteger(line.keyword_id)})
merge (from)-[r:attribute{paper_id:toInteger(line.paper_id),keyword_id:toInteger(line.keyword_id)}]->(to);




