package com.mertcaliskanyurek.englishwordbox.data.repository

import androidx.lifecycle.LiveData
import com.mertcaliskanyurek.englishwordbox.data.local.WordDAO
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.network.TranslationResponse
import com.mertcaliskanyurek.englishwordbox.data.network.TurengApi
import com.mertcaliskanyurek.englishwordbox.util.AppInfo
import com.mertcaliskanyurek.englishwordbox.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * Created by Mert Çalışkanyürek on 06,December,2021
 */

class WordRepository @Inject constructor(
    private val db: WordDAO,
    private val api: TurengApi
):IWordRepository {

    override fun getWord(level: WordLevel): Flow<WordModel> = db.getWordByLevel(level)

    override suspend fun insertAll(wordList: List<WordModel>) = db.insertAll(wordList)

    override suspend fun updateWord(word: WordModel) = db.updateWord(word)

    override fun searchWord(text: String): Flow<List<WordModel>> = db.searchWord(text)

    /*override fun translateWord(word: String, language: String): Flow<Resource<TranslationResponse>> = flow{
        try {
            emit(Resource.Loading<TranslationResponse>())

            val response = api.translate(language, AppInfo.INPUT_LANG,word, AppInfo.API_KEY)
            if(response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success<TranslationResponse>(it))
                }
            }
            else {
                response.errorBody()?.let {
                    emit(Resource.Error<TranslationResponse>(it.toString()))
                } ?: run {
                    emit(Resource.Error<TranslationResponse>(AppInfo.GENERAL_ERROR))
                }
            }

        }
        catch (e: Exception) {
            emit(Resource.Error<TranslationResponse>(e.localizedMessage?.toString()))
        }
    }*/

    override fun deleteAll() = db.nukeTable()
}