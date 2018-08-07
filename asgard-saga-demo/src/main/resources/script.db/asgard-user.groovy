package db

databaseChangeLog(logicalFilePath: 'asgard-user.groovy') {
    changeSet(id: '2018-08-01-create-table-asgard-user', author: 'jcalaz@163.com') {
        createTable(tableName: "asgard_user") {
            column(name: 'id', type: 'BIGINT UNSIGNED', remarks: 'ID', autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'username', type: 'VARCHAR(64)', remarks: 'name') {
                constraints(nullable: false)
            }
            column(name: 'password', type: 'VARCHAR(64)', remarks: 'name') {
                constraints(nullable: false)
            }

        }
    }
}
