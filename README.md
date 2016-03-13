# TableBrowser
##Introduction
This is a simple app that lets you browse a database table. It dumps all the table rows of a table in a recyclerview. The motivation
behind this was to explore the contacts database on an Android phone. However, the code is generic enough to be applied to any database 
table that supports the content provider API.
##Code organization
There are two recycler views hosted by a single activity. The top level recyclerView shows each row of the selected table as a row in the 
recyclerView. The row is a generic, textual information about the row. Things like column name, type and value are shown.
Clicking on each row shows details about the row in another recyclerView. In this, each column occupies a single row. It shows the details
about the column. 
##Actions
The floating action button helps you load the next table. When user is at the end of the list of tables, a click on the FAB will bring 
him or her backto the first table so it is a cyclic iteration.
##UnitTests
There are a few unit tests written to test components. It illustrates how to test a recyclerView adapter.
##References
http://developer.android.com/reference/android/support/v7/widget/RecyclerView.html
http://developer.android.com/training/testing/unit-testing/instrumented-unit-tests.html
http://developer.android.com/guide/topics/providers/contacts-provider.html
