package com.projectattitude.projectattitude.Objects;

/**
 * Created by henrywei on 3/23/17.
 */

/**
 * FilterDecoratorHandler used to handle implementation of the FilterDecorator
 */
public class FilterDecoratorHandler {

    /**
     * Given the decorator head and new decorator, replace an existing decorator with a new decorator with the same filterType.
     * If no existing decorator exists, add it to the decorator head and return it.
     * Postcondition: newDecorator will exist inside of the encapsulated decorators
     * @param decoratorHead - the decorator that encapsulates the other decorators
     * @param newDecorator - the decorator to replace an existing decorator (or be added)
     * @return Returns the new (or old) decoratorHead
     */
    public static FilterDecorator findAndReplace(FilterDecorator decoratorHead, FilterDecorator newDecorator){
        FilterDecorator tempDecorator = decoratorHead; //used to find decorator
        String tempType = newDecorator.getFilterType(); //newDecorator's type to search for
        while(tempDecorator!=null){
            if(tempDecorator.getFilterType().equals(tempType)){
                //If filter type found, replace filter parameter and return
                tempDecorator.setFilterParameter(newDecorator.getFilterParameter());
                return decoratorHead;
            }
            //If filter type not found yet, go to next encapsulated decorator
            tempDecorator = tempDecorator.getEnclosedDecorator();
        }
        //Not found, add it to decorator
        newDecorator.setEnclosedDecorator(decoratorHead);
        return newDecorator;
    }

    /**
     * Given the decorator head and new decorator, delete an existing decorator with
     * the corresponding filterType.
     * Postcondition: One decorator with corresponding filtertype will be deleted
     * in the encapsulated decorators. (Unless decorator not found)
     * @param decoratorHead - the decorator that encapsulates the other decorators
     * @param filterType - the type to find in decorator and delete
     * @return Returns the new (or old) decoratorHead
     */
    public static FilterDecorator findAndDelete(FilterDecorator decoratorHead, String filterType){
        FilterDecorator tempDecorator = decoratorHead; //used to find decorator
        FilterDecorator oldDecorator = null;
        while(tempDecorator != null){
            if(tempDecorator.getFilterType().equals(filterType)){
                //If a decorator was found with corresponding filterType, break out of loop
                break;
            }
            //Keep track of old decorator and move onto next decorator
            oldDecorator = tempDecorator;
            tempDecorator = tempDecorator.getEnclosedDecorator();
        }
        //CASE 1: tempDecorator == decoratorHead
        if(oldDecorator == null){
            //Return new decoratorhead to replace
            return decoratorHead.getEnclosedDecorator();
        }
        //CASE 2: Decorator not found
        if(tempDecorator == null){
            //Can't delete, so just return
            return decoratorHead;
        }
        //CASE 3: Decorator is found
        //Return to-be-deleted decorator's encapsulated decorator to decorator encapsulating to-be-deleted decorator
        oldDecorator.setEnclosedDecorator(tempDecorator.getEnclosedDecorator());
        return decoratorHead;
    }
}
