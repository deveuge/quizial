package com.deveuge.quizial.config.db;

import org.hibernate.tool.schema.spi.SchemaFilterProvider;

public class CustomSchemaFilterProvider implements SchemaFilterProvider {

	@Override
	public org.hibernate.tool.schema.spi.SchemaFilter getCreateFilter() {
		return CustomSchemaFilter.INSTANCE;
	}

	@Override
	public org.hibernate.tool.schema.spi.SchemaFilter getDropFilter() {
		return CustomSchemaFilter.INSTANCE;
	}

	@Override
	public org.hibernate.tool.schema.spi.SchemaFilter getMigrateFilter() {
		return CustomSchemaFilter.INSTANCE;
	}

	@Override
	public org.hibernate.tool.schema.spi.SchemaFilter getValidateFilter() {
		return CustomSchemaFilter.INSTANCE;
	}

}
