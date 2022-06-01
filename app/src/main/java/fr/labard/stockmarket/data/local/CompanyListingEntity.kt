package fr.labard.stockmarket.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CompanyListingEntity(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val symbol: String,
    val exchange: String
) {


}