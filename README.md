# RecursiveStringSort Implementation
    Implementation of a custom sorting Algorithm for
    sorting array of strings.
    
    The basic idea of the algorithm is as follows. When we receive an array or list of strings, we won't compare the strings to each other. What we want to do is transform a sorting problem into a problem of placing objects int groups. 

    To achieve this, we place the strings in different groups according to their characters. For example, in the 1st iteration, we can look at the 1st letter of each string and put in the first group all string that start with 'a', in the second group all strings that start with 'b', and so on. These distinct groups are called buckets, and it's important that the buckets have an order among themselves that corresponds to the order in which we want to sort. That is, the bucket 'a' comes before the bucket 'b', which comes before the bucket 'c', and so on.Figure 1 shows an example of placing strings in buckets.
<img width="419" alt="Screenshot 2023-12-23 191507" src="https://github.com/hugoConceicaoPT/RecursiveStringSort-Implementation/assets/154693289/8d555a7c-ffa9-4637-8b65-335cbe08bbbc">

    After placing all the strings in their respective buckets, we now have to sort each of the buckets. For example, to sort bucket 'a', we can use the same technique we used in the 1st iteration of the algorithm. However, we can't use the 1st character because all strings in bucket 'a' have the same 1st letter. Fortunately, there is a simple alternative. We will use the 2nd character of the string to decide the placement of strings starting with "a" in a new set of buckets. This sorting can be done by recursively calling the sorting algorithm for each of the buckets, adjusting the character index. Figure 2 illustrates the example of sorting bucket 'a', resulting in the creation of a new set of buckets and placing strings based on the 2nd character.
<img width="364" alt="Screenshot 2023-12-23 191514" src="https://github.com/hugoConceicaoPT/RecursiveStringSort-Implementation/assets/154693289/52a41843-d700-4272-b175-76b9cbd64c57">

    In the final phase of the algorithm, now that each bucket is sorted, we just need to traverse all the buckets from the 1st to the last bucket, and for each bucket, traverse each string. This order is the correct order, and therefore, it's enough to place the strings in the corresponding position of the initial array or list.
