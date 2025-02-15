import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from torchvision import datasets, transforms
from torch.utils.data import DataLoader


def get_data_loader( training = True):
    transform=transforms.Compose([
        transforms.ToTensor(),
        transforms.Normalize((0.1307,), (0.3081,))
        ])
    batch_size = 64
    if training:
        dataset =datasets.FashionMNIST('./data',train=True, download=True,transform=transform)
    else:
        dataset =datasets.FashionMNIST('./data', train=False, transform=transform)

    return DataLoader(dataset, batch_size=batch_size, shuffle=training)
    



def build_model():
    model = nn.Sequential(
        nn.Flatten(),
        nn.Linear(784, 128),
        nn.ReLU(),
        nn.Linear(128, 64),
        nn.ReLU(),
        nn.Linear(64, 10)
    )
    return model
   




def train_model(model, train_loader, criterion, T):
    criterion = nn.CrossEntropyLoss()
    opt = optim.SGD(model.parameters(), lr=0.001, momentum=0.9)
    model.train()

    for epoch in range(T):
        runningLoss = 0.0
        correct = 0
        total = 0
        for images, labels in train_loader:
            opt.zero_grad() #0 param gradients
            ouputs = model(images)
            loss = criterion(ouputs, labels)
            loss.backward()
            opt.step()

            runningLoss += loss.item() * images.size(0)  # Update total loss
            _, predicted = torch.max(ouputs.data, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()

        # Calculate average loss and accuracy
        epoch_loss = runningLoss / total
        epoch_acc = 100 * correct / total

        # Print training status
        print(f'Train Epoch: {epoch} Accuracy: {correct}/{total}({epoch_acc:.2f}%) Loss: {epoch_loss:.3f}')



def evaluate_model(model, test_loader, criterion, show_loss = True):
    model.eval()
    runningLoss = 0.0
    total = 0
    correct = 0
    with torch.no_grad():
        for data, labels in test_loader:
            outputs = model(data)
            loss = criterion(outputs, labels)
                

            runningLoss += loss.item() * data.size(0)  # Update total loss
            _, predicted = torch.max(outputs.data, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()

        epoch_loss = runningLoss / total
        epoch_acc = 100 * correct / total
    if show_loss:
        print(f'Average loss: {epoch_loss:.4f}')
    print(f'Accuracy:{epoch_acc:.2f}%')



def predict_label(model, test_images, index):
    model.eval()
    mimicImg = test_images[index].unsqueeze(0)

    with torch.no_grad():
        logits = model(mimicImg)
        prob = F.softmax(logits, dim=1)
        top_prob, top_id = torch.topk(prob,3)

    class_names = ['T-shirt/top','Trouser','Pullover','Dress','Coat','Sandal','Shirt'
,'Sneaker','Bag','Ankle Boot']
    
    for id, probs in zip(top_id[0], top_prob[0]):
        print(f'{class_names[id]}: {probs.item()*100: .2f}%')

if __name__ == '__main__':
    '''
    Feel free to write your own test code here to exaime the correctness of your functions. 
    Note that this part will not be graded.

    criterion = nn.CrossEntropyLoss()
    train_loader = get_data_loader()
    test_loader = get_data_loader(False)
    model = build_model()
    print(model)
    train_model = train_model(model, train_loader, criterion, T=5)
    print(train_model)
    eval_model = evaluate_model(model, test_loader, criterion, show_loss=True)
    print(eval_model)
    test_images = next(iter(test_loader))[0]
    predict = predict_label(model, test_images, 1)
    print(predict)
    '''
    
