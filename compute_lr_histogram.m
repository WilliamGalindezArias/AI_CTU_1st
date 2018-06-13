function lr_histogram = compute_lr_histogram(letter_char, Alphabet, images, labels, num_bins)
% lr_histogram = compute_lr_histogram(letter_char, Alphabet, images, labels, num_bins)
%
%   Calculates feature histogram.
%
%   Parameters:
%       letter_char is a character representing the letter whose feature 
%                   histogram we want to compute, e.g. 'C'
%       Alphabet - string of characters
%       images - images in 3d matrix of shape <h x w x n>
%       labels - labels of images, indices to Alphabet list, <1 x n>
%       num_bins - number of histogram bins
%   
%   Return: 
%       lr_histogram - counts of values in the corresponding bins, vector <1 x num_bins>
%
%   class support: integer classes

    letter_finder_1 = find(Alphabet == letter_char)
    find_labels_letter_1 = find(labels==letter_finder_1)

    % With this I find the number of columns in order to select the half of
    % it and find the X feature
   cols = size(images(:,:,1),2)
    % this computes de sum of each half then substract for one image
   x_feature = sum(sum(images(:,1:cols/2),2)) - sum(sum(images(:, (cols/2)+1:cols)),2)
    % this computes de sum of each half then substract for ALL images for a
    % specified letter
   x_feature_per_letter = sum(sum(images(:,1:cols/2, find_labels_letter_1(:)),2)) - sum(sum(images(:, (cols/2)+1:cols, find_labels_letter_1(:)),2))                   
    
    
   vector = reshape(x_feature_per_letter,1,length(x_feature_per_letter))
    
   
%   
    lr_histogram = hist(vector,num_bins);
%     

     
    
    
    
    
    
    
end
