package com.deveuge.quizial.config.db;

import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.spi.SchemaFilter;

public class CustomSchemaFilter implements SchemaFilter {
	
	public static final CustomSchemaFilter INSTANCE = new CustomSchemaFilter();

	@Override
	public boolean includeNamespace(Namespace namespace) {
		return true;
	}

	@Override
	public boolean includeTable(Table table) {
		return !table.getName().startsWith("vw_");
	}

	@Override
	public boolean includeSequence(Sequence sequence) {
		return true;
	}

}
