package controllers

/**
 * Created by Egor Nemchinov on 08.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
interface Controller {
    fun openFile(pathString: String)
    fun handleInput(input: String)
}