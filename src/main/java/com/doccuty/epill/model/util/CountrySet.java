/*
   Copyright (c) 2017 mac
   
   Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
   and associated documentation files (the "Software"), to deal in the Software without restriction, 
   including without limitation the rights to use, copy, modify, merge, publish, distribute, 
   sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
   furnished to do so, subject to the following conditions: 
   
   The above copyright notice and this permission notice shall be included in all copies or 
   substantial portions of the Software. 
   
   The Software shall be used for Good, not Evil. 
   
   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
   BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
   DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
   
package com.doccuty.epill.model.util;

import java.util.HashSet;
import com.doccuty.epill.model.Country;
import java.util.Collection;
import de.uniks.networkparser.list.NumberList;
import de.uniks.networkparser.list.ObjectSet;
import java.util.Collections;
import com.doccuty.epill.model.util.UserSet;
import com.doccuty.epill.user.User;

public class CountrySet extends HashSet<Country>
{
	protected Class<?> getTypClass() {
		return Country.class;
	}

   public CountrySet()
   {
      // empty
   }

   public CountrySet(Country... objects)
   {
      for (Country obj : objects)
      {
         this.add(obj);
      }
   }

   public CountrySet(Collection<Country> objects)
   {
      this.addAll(objects);
   }

   public static final CountrySet EMPTY_SET = new CountrySet();


   public String getEntryType()
   {
      return "com.doccuty.epill.model.Country";
   }


   @SuppressWarnings("unchecked")
   public CountrySet with(Object value)
   {
      if (value == null)
      {
         return this;
      }
      else if (value instanceof java.util.Collection)
      {
         this.addAll((Collection<Country>)value);
      }
      else if (value != null)
      {
         this.add((Country) value);
      }
      
      return this;
   }
   
   public CountrySet without(Country value)
   {
      this.remove(value);
      return this;
   }


   /**
    * Loop through the current set of Country objects and collect a list of the id attribute values. 
    * 
    * @return List of int objects reachable via id attribute
    */
   public NumberList getId()
   {
      NumberList result = new NumberList();
      
      for (Country obj : this)
      {
         result.add(obj.getId());
      }
      
      return result;
   }


   /**
    * Loop through the current set of Country objects and collect those Country objects where the id attribute matches the parameter value. 
    * 
    * @param value Search value
    * 
    * @return Subset of Country objects that match the parameter
    */
   public CountrySet filterId(int value)
   {
      CountrySet result = new CountrySet();
      
      for (Country obj : this)
      {
         if (value == obj.getId())
         {
            result.add(obj);
         }
      }
      
      return result;
   }


   /**
    * Loop through the current set of Country objects and collect those Country objects where the id attribute is between lower and upper. 
    * 
    * @param lower Lower bound 
    * @param upper Upper bound 
    * 
    * @return Subset of Country objects that match the parameter
    */
   public CountrySet filterId(int lower, int upper)
   {
      CountrySet result = new CountrySet();
      
      for (Country obj : this)
      {
         if (lower <= obj.getId() && obj.getId() <= upper)
         {
            result.add(obj);
         }
      }
      
      return result;
   }


   /**
    * Loop through the current set of Country objects and assign value to the id attribute of each of it. 
    * 
    * @param value New attribute value
    * 
    * @return Current set of Country objects now with new attribute values.
    */
   public CountrySet withId(int value)
   {
      for (Country obj : this)
      {
         obj.setId(value);
      }
      
      return this;
   }


   /**
    * Loop through the current set of Country objects and collect a list of the country attribute values. 
    * 
    * @return List of String objects reachable via country attribute
    */
   public ObjectSet getCountry()
   {
      ObjectSet result = new ObjectSet();
      
      for (Country obj : this)
      {
         result.add(obj.getCountry());
      }
      
      return result;
   }


   /**
    * Loop through the current set of Country objects and collect those Country objects where the country attribute matches the parameter value. 
    * 
    * @param value Search value
    * 
    * @return Subset of Country objects that match the parameter
    */
   public CountrySet filterCountry(String value)
   {
      CountrySet result = new CountrySet();
      
      for (Country obj : this)
      {
         if (value.equals(obj.getCountry()))
         {
            result.add(obj);
         }
      }
      
      return result;
   }


   /**
    * Loop through the current set of Country objects and collect those Country objects where the country attribute is between lower and upper. 
    * 
    * @param lower Lower bound 
    * @param upper Upper bound 
    * 
    * @return Subset of Country objects that match the parameter
    */
   public CountrySet filterCountry(String lower, String upper)
   {
      CountrySet result = new CountrySet();
      
      for (Country obj : this)
      {
         if (lower.compareTo(obj.getCountry()) <= 0 && obj.getCountry().compareTo(upper) <= 0)
         {
            result.add(obj);
         }
      }
      
      return result;
   }


   /**
    * Loop through the current set of Country objects and assign value to the country attribute of each of it. 
    * 
    * @param value New attribute value
    * 
    * @return Current set of Country objects now with new attribute values.
    */
   public CountrySet withCountry(String value)
   {
      for (Country obj : this)
      {
         obj.setCountry(value);
      }
      
      return this;
   }

   /**
    * Loop through the current set of Country objects and collect a set of the User objects reached via user. 
    * 
    * @return Set of User objects reachable via user
    */
   public UserSet getUser()
   {
      UserSet result = new UserSet();
      
      for (Country obj : this)
      {
         result.with(obj.getUser());
      }
      
      return result;
   }

   /**
    * Loop through the current set of Country objects and collect all contained objects with reference user pointing to the object passed as parameter. 
    * 
    * @param value The object required as user neighbor of the collected results. 
    * 
    * @return Set of User objects referring to value via user
    */
   public CountrySet filterUser(Object value)
   {
      ObjectSet neighbors = new ObjectSet();

      if (value instanceof Collection)
      {
         neighbors.addAll((Collection<?>) value);
      }
      else
      {
         neighbors.add(value);
      }
      
      CountrySet answer = new CountrySet();
      
      for (Country obj : this)
      {
         if ( ! Collections.disjoint(neighbors, obj.getUser()))
         {
            answer.add(obj);
         }
      }
      
      return answer;
   }

   /**
    * Loop through current set of ModelType objects and attach the Country object passed as parameter to the User attribute of each of it. 
    * 
    * @return The original set of ModelType objects now with the new neighbor attached to their User attributes.
    */
   public CountrySet withUser(User value)
   {
      for (Country obj : this)
      {
         obj.withUser(value);
      }
      
      return this;
   }

   /**
    * Loop through current set of ModelType objects and remove the Country object passed as parameter from the User attribute of each of it. 
    * 
    * @return The original set of ModelType objects now without the old neighbor.
    */
   public CountrySet withoutUser(User value)
   {
      for (Country obj : this)
      {
         obj.withoutUser(value);
      }
      
      return this;
   }

}
