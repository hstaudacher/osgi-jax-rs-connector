package com.eclipsesource.jaxrs.all.test;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;


@RunWith( ClasspathSuite.class )
@ClassnameFilters( { 
  ".*Test",
  ".*_Test",
  "!.TestSuite",
  "!.OSGiTests",
  "!.*OSGiTest"
} )
public class AllConnectorTestSuite {
}