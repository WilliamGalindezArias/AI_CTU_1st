function output = matrix_manip(A,B)
% output = matrix_manip(A,B)
%
%   Perform example matrix manipulations.
%
%   Parameters:
%       A - matrix, arbitrary shape
%       B - matrix, <2 x n>
%
%   Return:
%       output.A_transpose
%       output.A_3rd_col
%       output.A_slice
%       output.A_gr_inc
%       output.C
%       output.A_weighted_col_sum
%       output.D
%       output.D_select
   
    % 1. Find the transpose of the matrix A:
    output.A_transpose = A'

    % 2. Select the third column of the matrix A:
    output.A_3rd_col =  A(:,3)

    % 3. Select last two rows and last three columns of the matrix A and return the matrix in output.A_slice. 
    output.A_slice = A(end-1:end, end-2: end)

    % 4.Find all positions in A greater then 3 and increment them by 1 and add a column of ones to the matrix. Save the result to matrix A_gr_inc:
    new_m = A>3
    final_m = A+ new_m
    [r,c] = size(final_m) 
    new_col = ones(r,1)
    final_m = horzcat(final_m,new_col)
    output.A_gr_inc = final_m
    

    % 5. Create matrix C such that Ci,j=∑nk=1A_gr_inci,k⋅A_gr_incTk,j and store it in output.C. 
    output.C = output.A_gr_inc* (output.A_gr_inc')

    % 6. Compute ∑nc=1c⋅∑mr=1A_gr_incr,c:
    [r,m] = size(output.A_gr_inc)
    output.col_sum = sum(repmat(output.A_gr_inc(1:end, 1:end),1))
    output.constant = [1:m]
    output.A_weighted_col_sum = sum(output.constant .* output.col_sum)

    % 7. Subtract a vector (4,6)T from all columns of matrix B. Save the result to matrix output.D.
    output.D = repmat(B(:,1:end)-[4;6],1)

    % 8. Select all vectors in the matrix D, which have greater euclidean distance than the average euclidean distance.
    [row,col] = size(output.D)
    output.mrow_1 = output.D(1,:)
    output.mrow_2 = output.D(2,:)
    output.mrow_1_squared = output.D(1,:).^2
    output.mrow_2_squared = output.D(2,:).^2
    output.sum_of_rows = output.mrow_1_squared + output.mrow_2_squared
    output.sqrt_mat = sqrt(output.sum_of_rows)
    output.average_dist = sum(output.sqrt_mat)/col
    output_above_avg_index = find(output.sqrt_mat> output.average_dist)
    output.mrow_1_1 = output.mrow_1(output_above_avg_index)
    output.mrow_2_2 = output.mrow_2(output_above_avg_index)
    %this is the final output
    output.D_select = vertcat(output.mrow_1_1,output.mrow_2_2)

end
