#include <bits/stdc++.h>
using namespace std;

int N, A[505050];

long long cnt = 0;

// quicksort

void quicksort(int l, int r) {
	cnt++;
	if (l >= r) return;
	int pivot = A[(l + r) / 2];
	int i = l, j = r;
	while (i <= j) {
		while (A[i] < pivot) {
			i++;
			cnt++;
		}
		while (A[j] > pivot) {
			j--;
			cnt++;
		}
		if (i <= j) {
			cnt++;
			swap(A[i], A[j]);
			i++; j--;
		}
	}
	quicksort(l, j);
	quicksort(i, r);
}

int main() {
	ios_base::sync_with_stdio(false); cin.tie(nullptr);

	freopen("input.txt", "r", stdin);
	freopen("output.txt", "w", stdout);

	cin >> N;
	for (int i = 0; i < N; i++) cin >> A[i];

	quicksort(0, N - 1);

	cout << cnt << '\n';
}