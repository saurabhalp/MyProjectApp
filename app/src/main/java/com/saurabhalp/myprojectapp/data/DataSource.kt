package com.saurabhalp.myprojectapp.data
import com.saurabhalp.myprojectapp.Subject
import com.saurabhalp.myprojectapp.R
class DataSource (){
    fun loadSubjects() : List<Subject> {
        return listOf<Subject>(
            Subject(R.string.Subject1,"1"),
            Subject(R.string.Subject3,"1"),
            Subject(R.string.Subject4,"1"),
            Subject(R.string.Subject5,"5"),
            Subject(R.string.Subject6,"1"),
            Subject(R.string.Subject2,"1"),
        )
    }
}