/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.aws.s3.functions;

import org.jclouds.aws.AWSResponseException;
import org.jclouds.aws.domain.AWSError;
import org.testng.annotations.Test;

/**
 * @author Adrian Cole
 */
@Test(testName = "s3.ReturnTrueIfBucketAlreadyOwnedByYouTest")
public class ReturnTrueIfBucketAlreadyOwnedByYouTest {

   @Test
   void testBucketAlreadyOwnedByYouIsOk() throws Exception {
      Exception e = getErrorWithCode("BucketAlreadyOwnedByYou");
      assert !new ReturnFalseIfBucketAlreadyOwnedByYou().apply(e);
   }

   @Test(expectedExceptions = AWSResponseException.class)
   void testBlahIsNotOk() throws Exception {
      Exception e = getErrorWithCode("blah");
      new ReturnFalseIfBucketAlreadyOwnedByYou().apply(e);
   }

   private Exception getErrorWithCode(String code) {
      AWSError error = new AWSError();
      error.setCode(code);
      return new AWSResponseException(null, null, null, error);
   }
}