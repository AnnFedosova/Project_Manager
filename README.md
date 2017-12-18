# Levin Project Manager
Simple Project Manager App

## Run environment requirements
In order to run Project Manager you need to have:

- JRE 8 or higher
- WildFly 11 or higher
- PostgreSQL 10 or higher / Microsoft SQL Server

Work on younger versions of software is not guaranteed.

## Installation
### 1. Datasource configuration
You can edit datasource settings in src/main/resources/hibernate.cfg.xml file.

### 2. WildFly configuration
1. Install JDBC driver to Wildfly.
2. Add datasource to WildFly.
3. Add "ProjectManagerSec" security-domain to standalone.xml:

```xml
<security-domain name="ProjectManagerSec" cache-type="default">
    <authentication>
        <login-module code="Database" flag="required">
            <module-option name="dsJndiName" value= "Your datasource jndi-name" />
            <module-option name="principalsQuery" value="SELECT password from users WHERE login=?"/>
            <module-option name="rolesQuery" value="select role_name, 'Roles' from roles, user_roles, users where login=? AND user_roles.role_id = roles.id AND user_roles.user_id = users.id"/>
            <module-option name="hashAlgorithm" value="MD5"/>
            <module-option name="hashEncoding" value="base64"/>
        </login-module>
    </authentication>
</security-domain>
```
4. Disable WildFly welcome content. To do this, delete the host mapper that maps requests to "/" to the "welcome-content" handler:
```xml
<server name="default-server">  
     <host name="default-host" alias="localhost">  
          <location name="/" handler="welcome-content"/>    <!-- Delete this line! -->  
     </host>  
</server>  
```

Delete the welcome-content handler. Since this is the only handler, you can delete the whole <handlers> element:
```xml
<handlers>  
     <file name="welcome-content" path="${jboss.home.dir}/welcome-content"/>  
</handlers>
```


