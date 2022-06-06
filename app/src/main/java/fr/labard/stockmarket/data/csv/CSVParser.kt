package fr.labard.stockmarket.data.csv

import java.io.InputStream

interface CSVParser<T> {
    suspend fun parse(stram: InputStream): List<T>
}