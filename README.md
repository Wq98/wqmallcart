# wqmallcart
练手用的小项目前端用的nginx反向代理，springboot+springcloud+solr+elasticsearch+redis+jwt+gateway<br>
实现功能：商品分页实现（详情页没写），购物车加购、显示、删除，结算没写，用户登录注册，搜索商品<br>
parent---项目父类，统一依赖版本<br>
common---实体类和一些公用配置类<br>
gateway----网关，jwt工具类以及路由过滤<br>
product----商品操作<br>
redis---redis配置封装类<br>
search---搜素，es/solr(不全，都只实现了搜索)<br>
user---用户操作<br>
