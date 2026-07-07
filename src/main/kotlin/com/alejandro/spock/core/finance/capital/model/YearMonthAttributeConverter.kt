package com.alejandro.spock.core.finance.capital.model

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.YearMonth

@Converter(autoApply = true)
class YearMonthAttributeConverter : AttributeConverter<YearMonth, String> {
	override fun convertToDatabaseColumn(attribute: YearMonth?): String? = attribute?.toString()

	override fun convertToEntityAttribute(dbData: String?): YearMonth? = dbData?.let(YearMonth::parse)
}
