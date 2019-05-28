Project Details
------------------
* The REST API loads and parses navigation.json into memory once at application startup. 
* Creates a single API endpoint that takes a ID path parameter (/{ID}), here are some examples:
    1. /30day
    2. /30daymain
    3. /Training
* If the ID parameter is missing assumes it is "root"
* Using the ID the API finds the node and returns JSON for a tree that has been pruned using the following rules:

    1. The root and it's children are *always* included
    2. The path from the selected ID (node) to the root is included
    3. The children of other nodes are *not* included
* If the node cannot be found the endpoint returns 404

#### Pruning Examples

Given this initial tree:


![Initial Tree](start_tree.jpg)


The result when querying the red node is:


![Result Tree](result_tree.jpg)

Similarly for this node:

![Initial Tree](start_tree2.jpg)

Results in:

![Initial Tree](result_tree2.jpg)

Running Project on Eclipse
---------------------------
I used Eclipse for this. So you can import it as a project ("General-->Existing Project into Workspace").  I used Tomcat but you should be able to use pretty much any server to test. Used 1.8.x JDK and JRE versions. Sample URL -- http://localhost:8090/NavTree/navTree/store.  Tests folder (separate from the project tests package) has json responses for the IDs I ran.  

Approach/Steps:
                1. Read tree (with jackson)
                2. Traverse (when node with matching id found, ignore all future nodes except the immediate descendants of root).
                3. Prune the nodes that are on the left of the matching node

If you have any questions/issues, please reach out.  Thank you!


