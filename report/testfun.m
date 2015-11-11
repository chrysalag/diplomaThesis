A = [ 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 ; 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 ; 0 0 0 0 0 0 0 1 0 0 0 0 1 0 0 1 0 0 0 0 ; 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 1 0 0 0 0 ; 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 ; 0 0 1 1 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 ];
numRowsA = size(A, 1);
S = sum(A, 2)

for i = 1:numRowsA

        R(i, :) = A(i, :) / S(i);

end

Y = transpose(A)


numRowsY = size(Y, 1)

SY = sum(Y, 2)

for i = 1:numRowsY

        if SY(i) != 0

                T(i, :) = Y(i, :) / SY(i);

        else T(i, :) = Y(i, :);

        end

end

P = R * T;

PF = [4 0 0 0 3 1 ; 
      4 0 0 0 4 4 ; 
      1 0 0 0 1 3]

numRowsPFV = size(PF, 1)

SPFV = sum(PF, 2)

for i = 1:numRowsPFV

        if PF(i) != 0

                PF(i, :) = PF(i, :) / SPFV(i);

        else PF(i, :) = PF(i, :);

        end

end

C = [0 0 0 0 0.5 0.5 ;
     0 0 0 0  0   0  ;
     0 0 0 0  0   0  ;
     0 0 0 0  0   0  ;
   0.5 0 0 0  0  0.5 ;
   0.5 0 0 0 0.5  0 ];



a = 0.1

CR = 0.6*C

P = 0.3*P

CRP = CR + P

Rank = a*PF + PF*CRP

