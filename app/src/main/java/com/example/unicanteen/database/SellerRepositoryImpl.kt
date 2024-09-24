package com.example.unicanteen.database


class SellerRepositoryImpl(private val sellerDao: SellerDao) : SellerRepository {

    override suspend fun insertSeller(seller: Seller): Long {
        return sellerDao.insertSeller(seller)
    }

    override suspend fun updateSeller(seller: Seller) {
        sellerDao.updateSeller(seller)
    }

    override suspend fun deleteSeller(seller: Seller) {
        sellerDao.deleteSeller(seller)
    }

    override suspend fun getSellerById(sellerId: Int): Seller {
        return sellerDao.getSellerById(sellerId)
    }

    override suspend fun getSellersByUserId(userId: Int): List<Seller> {
        return sellerDao.getSellersByUserId(userId)
    }

    override suspend fun getAllSellers(): List<Seller> {
        return sellerDao.getAllSellers()
    }

    override suspend fun deleteSellersByUserId(userId: Int) {
        sellerDao.deleteSellersByUserId(userId)
    }

    override suspend fun getSellersByStatus(status: String): List<Seller> {
        return sellerDao.getSellersByStatus(status)
    }

    override suspend fun getSellersWithHighRating(rating: Double): List<Seller> {
        return sellerDao.getSellersWithHighRating(rating)
    }
    override suspend fun searchSellersByName(query: String): List<Seller> {
        return sellerDao.searchSellersByName(query)
    }
}
