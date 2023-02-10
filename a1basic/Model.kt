package com.example.a1enhanced
import javafx.beans.InvalidationListener
import javafx.beans.Observable


// Class to store each course's information.
class Course(val id: String, var name: String, var term: String, var grade: String):Comparable<Course> {
    var show: Boolean = true

    companion object {
        var curSort: String = "Course Code"
    }

    //Override CompareTo for sort algorithm
    override fun compareTo(other: Course): Int = when {
        curSort == "Course Code" -> compareValues(id.lowercase(), other.id.lowercase())
        curSort == "Term" && term.substring(1, 3) != other.term.substring(1, 3) -> term.substring(1, 3)
            .toInt() - other.term.substring(1, 3).toInt()
        curSort == "Term" && term[0] == other.term[0] -> 0
        curSort == "Term" && (term[0] == 'F' || other.term[0] == 'S') -> -1
        curSort == "Term" && (term[0] == 'S' || other.term[0] == 'F') -> 1
        grade.uppercase() == "WD" -> -1
        other.grade.uppercase() == "WD" -> 1
        else -> grade.toInt() - other.grade.toInt()
    }
}


class CourseException(var idValid: Boolean = true, var termValid: Boolean = true, var gradeValid: Boolean = true): Exception()




class Model: Observable {
    /////////////////////////////////////////  ALL LISTENER INFORMATION //////////////////////////////////////////////
    private val listeners = mutableListOf<InvalidationListener?>()
    override fun addListener(listener: InvalidationListener?) {
        listeners.add(listener)
    }
    override fun removeListener(listener: InvalidationListener?) {
        listeners.remove(listener)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var courses = mutableMapOf<String, Course>()
    /*private var courses = mutableMapOf<String, Course>("CS135" to Course("CS135", "", "F21", "wd"),
                                                        "BU111" to Course("BU111", "", "F21", "70"),
                                                        "EC120" to Course("EC120", "", "F20", "78"),
                                                        "PD1" to Course("PD1", "", "S23", "wd"),
                                                        "math 135" to Course("math 135", "", "W22", "12"),
                                                        "math 239" to Course("math 239", "", "W23", "wd"),
                                                        "CO 350" to Course("CO 350", "", "F22", "90"))*/
    private var curFilter: String = ""
    private var curWD: Boolean = true
    private var courseCount = 0
    private var gradeTotal = 0.0
    private var failedCount = 0
    private var numOfWDs = 0
    private var cumGPA = 0.0


    // updates the data of "id" in courses map
    fun updateCourse(id: String, name: String, term: String, grade: String){


        if(grade.uppercase() == "WD" || grade.toInt() in 0..100){
            courses[id]!!.name = name
            courses[id]!!.term = term
            courses[id]!!.grade = grade
            sortCourse(Course.curSort, curFilter, curWD)
            listeners.forEach{it?.invalidated(this)}
        } else {
            throw Exception("Invalid Grade")
        }
    }

    // sort and filters courses depending on user's input
    fun sortCourse(sort: String, filter: String, wd: Boolean) {
        Course.curSort = sort
        curFilter = filter
        //curWD = wd
        if(curWD == wd) {
            courses = if (Course.curSort == "Grade Descending") {
                courses.toList().sortedBy { (_, value) -> value }.reversed().toMap().toMutableMap()
            } else {
                courses.toList().sortedBy { (_, value) -> value }.toMap().toMutableMap()
            }
        }

        curWD = wd

        courses.forEach {
            if(wd) it.value.show = true
            else it.value.show = it.value.grade.uppercase() != "WD"
        }

        when (curFilter) {
            "All Courses" -> courses.forEach { it.value.show = it.value.show }
            "CS Courses Only" -> courses.forEach{ it.value.show = it.value.show && it.key.substring(0, 2).lowercase() == "cs" }
            "Math Courses Only" -> courses.forEach { it.value.show = it.value.show && (it.key.substring(0, 2).lowercase() == "cs" ||
                    it.key.substring(0, 2).lowercase() == "co" || (it.key.length >= 4 && it.key.substring(0, 4).lowercase() == "math") ||
                    (it.key.length >= 4 && it.key.substring(0, 4).lowercase() == "stat"))}
            else -> courses.forEach { it.value.show = it.value.show && !(it.key.substring(0, 2).lowercase() == "cs" ||
                    it.key.substring(0, 2).lowercase() == "co" || (it.key.length >= 4 && it.key.substring(0, 4).lowercase() == "math") ||
                    (it.key.length >= 4 && it.key.substring(0, 4).lowercase() == "stat")) }
        }

        courseCount = 0
        gradeTotal = 0.0
        failedCount = 0
        numOfWDs = 0
        cumGPA = 0.0

        courses.forEach{
            if(it.value.show) {
                if (it.value.grade.uppercase() != "WD") {
                    if (it.value.grade.toInt() < 50) {
                        failedCount++
                    }

                    cumGPA += findGPA(it.value.grade.toInt())
                    courseCount++
                    gradeTotal += it.value.grade.toDouble()
                } else {
                    numOfWDs++
                }
            }
        }


        listeners.forEach{it?.invalidated(this)}
    }

    // deletes course with course id "id"
    fun removeCourse(id: String){
        courses.remove(id)
        sortCourse(Course.curSort, curFilter, curWD)
        listeners.forEach{it?.invalidated(this)}
    }


    // adds course to course array, sorts and filters, then updates display
    fun addCourse(id: String, name: String, term: String, grade: String) {
        var isProblem = false
        val exc = CourseException()

        if(id == "") {
            isProblem = true
            exc.idValid = false
        }

        if (term == ""){
            isProblem = true
            exc.termValid = false
        }

        if(grade.uppercase() != "WD" && grade.toInt() !in 0..100){
            // grade too small or big
            isProblem = true
            exc.gradeValid = false
        }

        if(isProblem){
            throw exc
        } else {
            courses[id] = Course(id, name, term, grade)
            sortCourse(Course.curSort, curFilter, curWD)
            listeners.forEach { it?.invalidated(this) }
        }
    }


    fun getCourses(): Map<String, Course> {
        return courses
    }

    fun setCurFilter(newFilter: String) { curFilter = newFilter }
    fun setCurWD(newWD: Boolean) { curWD = newWD}

    fun getCurWD(): Boolean { return curWD }

    fun getCourseCount(): Int { return courseCount}
    fun getGradeTotal(): Double { return gradeTotal }
    fun getFailedCount(): Int { return failedCount }
    fun getNumOfWDs(): Int { return numOfWDs }
    fun getCumGPA(): Double { return cumGPA}
    private fun findGPA(g: Int): Double {
        val ret: Double  = when (g) {
            in 0..49 -> 0.0
            in 50..52 -> 0.7
            in 53..56 -> 1.0
            in 57..59 -> 1.3
            in 60..62 -> 1.7
            in 63..66 -> 2.0
            in 67..69 -> 2.3
            in 70..72 -> 2.7
            in 73 .. 76 -> 3.0
            in 77..79 -> 3.3
            in 80..84-> 3.7
            in 85..89 -> 3.9
            else -> 4.0
        }
        return ret

    }





}


