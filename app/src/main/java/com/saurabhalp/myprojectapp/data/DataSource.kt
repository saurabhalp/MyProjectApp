package com.saurabhalp.myprojectapp.data
import com.saurabhalp.myprojectapp.Subject
import com.saurabhalp.myprojectapp.R
class DataSource (){
    fun loadSubjects() : List<Subject> {
        return listOf<Subject>(
            Subject(R.string.Subject1,"1"),
            Subject(R.string.Subject2,"2"),
            Subject(R.string.Subject3,"3"),
            Subject(R.string.Subject4,"4"),
            Subject(R.string.Subject5,"5"),
            Subject(R.string.Subject6,"6"),
        )
    }
}