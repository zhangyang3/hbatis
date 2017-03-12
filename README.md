# 功能概述
支持hibernate将sql或hql语句从java文件提取到xml或注解中。
# sql编写
## Dao文件
```java
@Dao
public interface PersonDao {
	List<PersonDto> selectList2(Page<PersonDto> page, @Param("ids") String[] ids);

	List<PersonDto> selectList(Page<PersonDto> page, @Param("ids") String[] ids);

	List<PersonDto> findPersonByCollection(@Param("ids") String[] ids, @Param("name") PersonDto person);

	List<PersonPo> findPersonByCollection2(List<String> col);

	PersonPo findPersonById(PersonPo person);

	List<PersonPo> findPersonByName(@Param("Pname") String name);

	PersonPo findPersonByIdAndName(@Param("name") String name, @Param("age") int age);

	void insertPerson(PersonPo person);

	void updatePerson(PersonPo person);

	void deletePerson(PersonPo person);

	List<PersonPo> findPersonByCollection3(String[] strings);

}
```
***Dao***为标记注解，让spring识别。  
***Param***给参数定义名称。  
- **入参个数为1，则不需要加Parm**
    - 参数类型为*String*时，默认名称为_str
    - 参数类型为*long,Long,double,Double,char,Charater,int,Integer*时，默认名称为_num。
    - 参数类型为*com.imzy.bean.Page<T>*时，默认名称为_page。
- **入参个数为2，则一定需要加上Param。**
## xml模式
```xml
<?xml version="1.0" encoding="UTF-8"?>
<sql namespace="com.imzy.test.dao.PersonDao">
	<seg id="seg2">
		1=1
	</seg>
	
	<select id="findPersonById" lang="hql">
		from PersonPo where id=#{_obj.id}
	</select>
	
	<select id="selectList">
		select * from person
	</select>

	<select id="findPersonByCollection">
		select * from person where <include refid="seg2" />
		<foreach collection="ids" item="item" sperator="," prefix="and id in (" suffix=")">
			#{item}
		</foreach>
		<if test="name.name != ''">
			and name = #{name.name}
		</if>
	</select>

	<select id="findPersonByCollection2">
		select * from person where
		<include refid="seg2" />
		<foreach collection="_list" item="item" sperator=","
			prefix="and card in (" suffix=")">
			#{item}
		</foreach>
	</select>


	<insert id="insertPerson">
		insert into person(age,name)
		values(#{_obj.age},#{_obj.name})
	</insert>

	<update id="updatePerson">
		update person set name=#{_obj.name}
		where id=#{_obj.id}
	</update>

	<delete id="deletePerson">
		delete from person where id=#{_obj.id}
	</delete>
</sql>
```
+ *sql*标签的*namespace*填写对应Dao的全类名。
    - lang属性，默认值为sql。如果为sql，则整个xml默认为sql语句。如果为hql，则整个xml默认为hql语句。
+ *seg*用于定义一个sql或hql片段，被其他dml标签引用。
+ *insert,update,delete,select*标签中可以包含*if,foreach,include*标签。
    - id属性，用来确定当前对应Dao的哪个方法。
    - lang属性，sql或hql。如果不填写，继承sql标签的lang值
+ *if,foreach,include*标签类似于mybatis中用法。

## annotation模式
```java
@Dao
public interface PersonDao2 {

	@Select("select * from person where id=#{id}")
	List<PersonDto> selectList(Page<PersonDto> page, @Param("id") String id);

	@Select(value = "from PersonPo where id=#{id}", type = LangType.HQL)
	List<PersonDto> selectList2(Page<PersonDto> page, @Param("id") Integer id);

	@Insert("insert into person(age,name) values(#{p.age},#{p.name})")
	void insertPerson(@Param("p") PersonPo person);

}
```
java文件的修改，不会引起sql语句发生变化。所以建议创建对应的xml。对xml发生任何修改，hbatis都会对xml和annotation的sql语句进行重构，从而确保开发者能够在不重启的前提下，实时看到自己的sql语句效果。
# spring配置
生产环境中只需要配置自动扫描，开发时可以配置xml监听
## 自动扫描配置

```xml
<bean class="com.imzy.bean.config.AutoScanConfiger" init-method="init">
	<property name="annotationClass" value="com.imzy.annotation.Dao"></property>
	<property name="sessionFactory" ref="sessionFactory"></property>
	<property name="basePackages">
		<array>
			<value>com.imzy.test.dao</value>
		</array>
	</property>
</bean>
```
***annotationClass***是Dao接口上的注解。开发人员可以使用hbatis默认值*com.imzy.annotation.Dao.class*，也可以使用其他自定义注解。  
***sessionFactory***由hibernate提供。  
***basePackages***表示需要扫描的基包名称。
## xml监听加载
开发时经常会修改xml中的sql语句，配置此项hbatis会自动监听xml的修改。每次调用的sql总是xml最新的值。
```xml
<bean class="com.imzy.help.sqlmonitor.SqllMonitor" init-method="init">
	<property name="basePackages">
		<array>
			<value>com.imzy.test.dao</value>
		</array>
	</property>
</bean>
```
***basePackages***表示需要扫描的基包名称。
